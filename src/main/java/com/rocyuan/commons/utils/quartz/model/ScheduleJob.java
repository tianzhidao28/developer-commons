/**
 *
 * Copyright (c) 2016, rocyuan, admin@rocyuan.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rocyuan.commons.utils.quartz.model;


import java.util.Date;

/**
 * 初始化 任务信息 ; 动态配置在数据库
 *
 * @author yuanzp@jpush.cn 2015-4-14
 */


public class ScheduleJob  {

	/** 任务id */
	/** 任务名称 */
	private String jobName;

	/** 执行那个方法 */
	private String method;
	/** 任务分组 */
	private String jobGroup;

	/** 触发器 */
	private String jobTrigger;

	/** 任务状态 */
	private String status;

	// 哪一个类
	private String springBeanId;

	// beanId 和className 至少一个不空 ;
	private String className;

	/** 任务运行时间表达式 */
	private String cronExpression;
	/** 是否异步 */
	private Boolean isSync;
	/** 任务描述 */
	private String description;
	/** 创建时间 */
	//@Temporal(TemporalType.TIMESTAMP)
	private Date createTime = new Date();
	/** 修改时间 */
	//@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

	private String param ;

	public ScheduleJob() {

	}


	public ScheduleJob(String jobName, String jobGroup, String className,
			String method, String cronExpression,
			String description,String param ,  Boolean isSync) {
		super();
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.className = className;
		this.method = method;
		this.cronExpression = cronExpression;
		this.isSync = isSync;
		this.description = description;
		this.param = param ;
	}


	public ScheduleJob(String jobName, String jobGroup, String springBeanId,
			String method, String cronExpression, Boolean isSync,
			String description,String param) {
		super();
		this.jobName = jobName;
		this.method = method;
		this.jobGroup = jobGroup;
		this.springBeanId = springBeanId;
		this.cronExpression = cronExpression;
		this.isSync = isSync;
		this.description = description;
		this.param = param ;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobTrigger() {
		return jobTrigger;
	}

	public void setJobTrigger(String jobTrigger) {
		this.jobTrigger = jobTrigger;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(Boolean isSync) {
		this.isSync = isSync;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getSpringBeanId() {
		return springBeanId;
	}

	public void setSpringBeanId(String springBeanId) {
		this.springBeanId = springBeanId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	public String getParam() {
		return param;
	}


	public void setParam(String param) {
		this.param = param;
	}


	@Override
	public String toString() {
		return "ScheduleJob [jobName=" + jobName + ", method=" + method
				+ ", jobGroup=" + jobGroup
				+ ", springBeanId=" + springBeanId
				+ ", className=" + className + ", cronExpression="
				+ cronExpression +", description="
				+ description + ", createTime=" + createTime + ", param=" + param + "]";
	}

}
