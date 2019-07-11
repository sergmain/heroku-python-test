/*
 * Metaheuristic, Copyright (C) 2017-2019  Serge Maslyukov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ai.metaheuristic.simple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Globals {

    private final Environment env;

    @Value("${mh.launchpad.is-ssl-required:#{true}}")
    public boolean isSslRequired = true;

    public Globals(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {

        String sslRequiredAsStr = env.getProperty("MH_IS_SSL_REQUIRED");
        if (sslRequiredAsStr!=null && !sslRequiredAsStr.isBlank()) {
            try {
                isSslRequired = Boolean.valueOf(sslRequiredAsStr);
            } catch (Throwable th) {
                System.err.println("Wrong value in env MH_IS_SSL_REQUIRED, must be boolean (true/false), " +
                        "actual: " + sslRequiredAsStr+". Will be used 'true' as value.");
                isSslRequired = true;
            }
        }
    }
}
