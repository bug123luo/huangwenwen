/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  GetBulletNumberReqService.java   
 * @Package com.tct.service.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年11月2日 上午11:47:32   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.tct.service.impl;

import java.util.Map;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.tct.codec.protocol.pojo.GetBulletNumberReqMessage;
import com.tct.codec.protocol.pojo.SimpleReplyMessage;
import com.tct.db.dao.MessageRecordsDao;
import com.tct.jms.producer.OutQueueSender;
import com.tct.util.StringConstant;
import lombok.extern.slf4j.Slf4j;


/**   
 * @ClassName:  GetBulletNumberReqService   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年11月2日 上午11:47:32   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@Service("getBulletNumberReqService")
@Scope("prototype")
@Slf4j
public class GetBulletNumberReqService implements TemplateService {

	@Autowired
	@Qualifier("stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	@Qualifier("jedisTemplate")
	private RedisTemplate<String,Map<String, ?>> jedisTemplate;
	
	@Resource
	private OutQueueSender outQueueSender;
	
	@Resource
	@Qualifier("outQueueDestination")
	private Destination outQueueDestination;
	
	@Autowired
	private MessageRecordsDao msgDao;
	
	/**   
	 * <p>Title: handleCodeMsg</p>   
	 * <p>Description: </p>   
	 * @param msg
	 * @throws Exception   
	 * @see com.tct.service.impl.TemplateService#handleCodeMsg(com.alibaba.fastjson.JSONObject)   
	 */
	@Override
	public void handleCodeMsg(Object msg) throws Exception {
		GetBulletNumberReqMessage gbnrMsg = (GetBulletNumberReqMessage)msg;
		
		String sessionToken = stringRedisTemplate.opsForValue().get(gbnrMsg.getUniqueIdentification());
		
		if(null==sessionToken) {
			log.info("腕表没有注册，请先注册");
			return;
		}
		
		msgDao.insertSelective(gbnrMsg);
		
		gbnrMsg.setSessionToken(sessionToken);
		SimpleReplyMessage simpleReplyMessage =constructReply(gbnrMsg);
		outQueueSender.sendMessage(outQueueDestination, JSONObject.toJSONString(simpleReplyMessage));
	}
	
	public SimpleReplyMessage constructReply(GetBulletNumberReqMessage gbnrMsg) {
		
		SimpleReplyMessage simpleReplyMessage = new SimpleReplyMessage();
		BeanUtils.copyProperties(gbnrMsg,simpleReplyMessage);
		String replyBody = StringConstant.MSG_BODY_PREFIX+gbnrMsg.getMessageBody().getGunMac()
		+StringConstant.MSG_BODY_SEPARATOR+gbnrMsg.getMessageBody().getAuthCode()
		+StringConstant.MSG_BODY_SUFFIX;
		simpleReplyMessage.setMessageBody(replyBody);
		
		return simpleReplyMessage;
	}

}
