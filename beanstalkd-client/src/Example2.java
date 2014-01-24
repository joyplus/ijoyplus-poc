import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import com.trendrr.beanstalk.BeanstalkJob;
import com.trendrr.beanstalk.BeanstalkPool;

/**
 * @author weikey
 * @created JAN 24, 2014
 * 
 */

public class Example2 {
	protected static Log log = LogFactory.getLog(Example2.class);

	public static void putJob() {
		BeanstalkClient client = new BeanstalkClient("localhost", 11111,
				"example");
		log.info("Putting a job");
		try {
			client.put(1l, 0, 5000, ("this is some data:" + (new Random()
					.nextInt())).getBytes());
		} catch (BeanstalkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.close(); // closes the connection
		}
	}

	public static void getJob() {
		BeanstalkClient client = new BeanstalkClient("localhost", 11111,
				"example");

		try {
			BeanstalkJob job = client.reserve(60);
			log.info("GOt job: " + job);
			log.info("Job id" + job.getId());
			log.info("Job data:" + new String(job.getData()));
			client.deleteJob(job);
		} catch (BeanstalkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.close(); // closes the connection
		}
	}

	public static void putJobByPool() {
		BeanstalkPool pool = new BeanstalkPool("localhost", 11111, 30, // poolsize
				"example" // tube to use
		);
		log.info("Putting a job");
		BeanstalkClient client = null;
		try {
			client = pool.getClient();
			client.put(1l, 0, 5000, ("this is some data:" + (new Random()
					.nextInt())).getBytes());
		} catch (BeanstalkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.close(); // returns the connection to the pool
		}
	}

	public static void getJobByPool() {
		BeanstalkPool pool = new BeanstalkPool("localhost", 11111, 30, // poolsize
				"example" // tube to use
		);
		BeanstalkClient client = null;
		try {
			client = pool.getClient();
			BeanstalkJob job = client.reserve(60);
			log.info("GOt job: " + job);
			log.info("Job id" + job.getId());
			log.info("Job data:" + new String(job.getData()));
			client.deleteJob(job);
		} catch (BeanstalkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.close(); // returns the connection to the pool
		}
	}

	public static void main(String[] args) {
		putJob();
		getJob();
		putJobByPool();
		getJobByPool();
	}

}
