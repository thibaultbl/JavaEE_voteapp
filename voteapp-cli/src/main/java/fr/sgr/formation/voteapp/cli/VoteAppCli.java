package fr.sgr.formation.voteapp.cli;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VoteAppCli implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(VoteAppCli.class);

    public static void main(String args[]) {
        SpringApplication.run(VoteAppCli.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("===========> Application démarrée");
        log.info("===========> Paramètres: {}", 
        		Arrays.stream(strings)
        			.collect(Collectors.joining(", ")));
    }
}
