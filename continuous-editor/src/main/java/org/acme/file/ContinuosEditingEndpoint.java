package org.acme.file;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Path("")
public class ContinuosEditingEndpoint {

	@Inject
	Logger log;

	@GET
	@Path("/sysresources")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSystemResources() {
		long memory = Runtime.getRuntime().maxMemory();
		int cores = Runtime.getRuntime().availableProcessors();
		return " Memory: " + (memory / 1024 / 1024) +
				" Cores: " + cores + "\n";
	}

	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	@RestStreamElementType(MediaType.TEXT_PLAIN)
	public Multi<String> foreverEdit(@QueryParam("name") @DefaultValue("5") String name, @QueryParam("size") @DefaultValue("1000") int size, @QueryParam("interval") @DefaultValue("1") long interval) {
		byte[] data = new byte[size];
		Random r = new Random();
		r.nextBytes(data);
		try {
			FileOutputStream fos = new FileOutputStream(name);
			fos.write(data);
			fos.close();
		} catch (Exception ex) {
			log.error(ex);
		}

		FileProcessor processedFiled = new FileProcessor(name, size, 0, 0, 0);
		processedFiled.start();
		long startTime = System.currentTimeMillis();
		StringBuffer answer = new StringBuffer(
				"Duration (seconds), Count, Checksum, First byte");

		return Multi.createBy().repeating()
				.supplier(
						() -> new AtomicInteger(1),
						i -> {
							try {
								Thread.sleep(Duration.ofSeconds(interval).toMillis());
							} catch (InterruptedException iex) {
								log.error("Monitoring interrupted by", iex);
							}
							long duration = (System.currentTimeMillis() - startTime) / 1000;
							answer.append("\n").append(duration).append(", ").append(processedFiled.iteration).append(", ").append(processedFiled.checksum).append(", ").append(processedFiled.firstByte);
							return answer.toString();
						}).atMost(interval*20);

	}
}