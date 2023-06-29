package org.acme.prime;

import io.smallrye.mutiny.Multi;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Path("")
public class PrimeEndpoint {

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
	public Multi<String> runPrimes(@QueryParam("rangeMax") @DefaultValue("5") long rangeMax, @QueryParam("interval") @DefaultValue("1") long interval) {
		StringBuffer answer = new StringBuffer(String.format("Searching in a range up to %s for %s seconds!\n", rangeMax, interval));
		PrimeFinder generator = new PrimeFinder(rangeMax, 0, 0);
		generator.start();
		long startTime = System.currentTimeMillis();
		answer.append("\n").append(
				"Duration (seconds), Primes Found, Max Prime Number");
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
							answer.append("\n").append(duration).append(", ").append(generator.primeCount).append(", ").append(generator.primeMax);
							return answer.toString();
						}).atMost(interval*10);
	}

}
