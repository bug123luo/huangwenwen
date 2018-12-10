/**  
 * All rights Reserved, Designed By www.tct.com
 * @Title:  Cat.java   
 * @Package com.lcclovehww.springboot.chapter3.pojo   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 泰源云景科技     
 * @date:   2018年12月10日 下午2:55:20   
 * @version V1.0 
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技内部传阅，禁止外泄以及用于其他的商业目
 */
package com.lcclovehww.springboot.chapter3.pojo;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.lcclovehww.springboot.chapter3.pojo.definition.Animal;

/**   
 * @ClassName:  Cat   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 泰源云景
 * @date:   2018年12月10日 下午2:55:20   
 *     
 * @Copyright: 2018 www.tct.com Inc. All rights reserved. 
 * 注意：本内容仅限于泰源云景科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@Component
@Primary
public class Cat implements Animal {

	/**   
	 * <p>Title: use</p>   
	 * <p>Description: </p>      
	 * @see com.lcclovehww.springboot.chapter3.pojo.definition.Animal#use()   
	 */
	@Override
	public void use() {
		System.out.println("猫{"+Cat.class.getSimpleName()+"}是抓老鼠");
	}

}
