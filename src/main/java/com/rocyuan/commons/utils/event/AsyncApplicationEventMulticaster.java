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
package com.rocyuan.commons.utils.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.AbstractApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.Executor;

/**
 *
 * @author yuanzp@jpush.cn
 * @date 2015-7-23
 * @desc 异步事件处理
 * Spring默认的事件广播器SimpleApplicationEventMulticaster
 * 由于SimpleApplicationEventMulticaster的taskExecutor的实现类是SyncTaskExecutor，因此，事件监听器对事件的处理，是同步进行的
 */
public class AsyncApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

	TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void multicastEvent(final ApplicationEvent event) {
		for (final ApplicationListener listener : getApplicationListeners(event)) {
			Executor executor = getTaskExecutor();
			if (executor != null) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						listener.onApplicationEvent(event);
					}
				});
			}
			else {
				listener.onApplicationEvent(event);
			}
		}

	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		 this.taskExecutor = (taskExecutor != null ? taskExecutor : new SimpleAsyncTaskExecutor());
	}



}
