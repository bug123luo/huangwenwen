/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  OutWarehouseReqService.java   
 * @Package com.tct.service.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年11月2日 上午11:48:38   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.tct.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tct.codec.protocol.pojo.OutWarehouseReqMessage;
import com.tct.db.dao.OutWarehouseDao;
import com.tct.db.po.AppGunCustom;
import com.tct.db.po.AppGunCustomQueryVo;
import com.tct.db.po.GunCustom;
import com.tct.db.po.GunCustomQueryVo;
import com.tct.jms.producer.OutQueueSender;


/**   
 * @ClassName:  OutWarehouseReqService   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年11月2日 上午11:48:38   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@Service("outWarehouseReqService")
@Scope("prototype")
public class OutWarehouseReqService implements TemplateService {

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
	OutWarehouseDao outWarehouseDao;
	
	/**   
	 * <p>Title: handleCodeMsg</p>   
	 * <p>Description: </p>   
	 * @param msg
	 * @throws Exception   
	 * @see com.tct.service.impl.TemplateService#handleCodeMsg(com.alibaba.fastjson.JSONObject)   
	 */
	@Override
	public void handleCodeMsg(Object msg) throws Exception {
		OutWarehouseReqMessage oWReqMsg = (OutWarehouseReqMessage)msg;
		
		AppGunCustomQueryVo appGunCustomQueryVo = new AppGunCustomQueryVo();
		AppGunCustom appGunCustom = new AppGunCustom();
		appGunCustom.setGunId(Integer.valueOf(oWReqMsg.getMessageBody().getGunId()));
		appGunCustom.setAllotState(Integer.valueOf(1));
		appGunCustomQueryVo.setAppGunCustom(appGunCustom);
		
		GunCustomQueryVo gunCustomQueryVo = new GunCustomQueryVo();
		GunCustom gunCustom = new GunCustom();
		gunCustom.setBluetoothMac(oWReqMsg.getMessageBody().getGunMac());
		gunCustomQueryVo.setGunCustom(gunCustom);
		
		outWarehouseDao.insertWarehouseRecords(appGunCustomQueryVo, gunCustomQueryVo);
		outQueueSender.sendMessage(outQueueDestination, JSONObject.toJSONString(oWReqMsg));
	}

}
