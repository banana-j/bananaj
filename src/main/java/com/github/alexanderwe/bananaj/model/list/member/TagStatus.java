/**
 * @author weljoda
 * @date 28.03.2019
 */

package com.github.alexanderwe.bananaj.model.list.member;

/**
 * @author weljoda
 *
 */
public enum TagStatus{
	ACTIVE("active"), INACTIVE("inactive");

	private String status;
    
    TagStatus( String status ){ 
		this.status = status; 
	}
		
	public String toString(){ 
		return status; 
	}
}
