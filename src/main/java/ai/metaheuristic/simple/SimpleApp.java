package ai.metaheuristic.simple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Serge
 * Date: 6/23/2019
 * Time: 10:13 PM
 */
@SpringBootApplication
@Controller
@Slf4j
@RequiredArgsConstructor
public class SimpleApp {

    public final ExecProcessService execProcessService;

    @Configuration
    public static class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

        private final Globals globals;

        public SpringSecurityConfig(Globals globals) {
            this.globals = globals;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/**/**").permitAll()
            ;
            if (globals.isSslRequired) {
                http.requiresChannel().antMatchers("/**").requiresSecure();
            }
        }
    }
    @RequestMapping("/")
    public @ResponseBody String index1() {
        return "Hello, world!";
    }

    @RequestMapping("/version")
    public @ResponseBody String version() throws IOException, InterruptedException {
        File execDir = new File("target/exec-dir");
        if (!execDir.exists()) {
            execDir.mkdirs();
        }
        File consoleLogFile = File.createTempFile("console-", ".log", execDir);
        ExecProcessService.SnippetExecResult r = execProcessService.execCommand(
                List.of("python", "--version"), execDir, consoleLogFile, null, "python");

        return "Version is: isOk: " + r.isOk +", console: " + r.console;
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleApp.class, args);
    }

}