package com.zouhao.util;

import java.util.Collection;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNUtil {
	private SVNRepository repository;
	/**
	 * 登录svn仓库,获取相应信息
	 */
	public boolean login(String svnRoot,String username,String password){
		try {  
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnRoot));  
			// 身份验证  
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password.toCharArray());  
			// 创建身份验证管理器  
			repository.setAuthenticationManager(authManager);  
			return true;
		} catch (SVNException e) { 
			e.printStackTrace();
			return false;
		}  
	}
	/**
	 * 根据起始版本号和结束版本号,获取中间的log日志
	 * @param int start
	 * @param int end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<SVNLogEntry> getCollectionLog(int start,int end){
		try{
			return repository.log( new String[] { "" } , null , start , end , true , true );	
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
