package cn.jpush.commons.utils.event;

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
