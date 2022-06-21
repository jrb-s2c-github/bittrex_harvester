# bittrex_harvester
Collection of Spring Boot applications to receive and interpret data from bittrex.com in a reactive way on a kubernetes cluster.

Uses Hazelcast as means of communication between the various application so that there can be multiple instances of ai_bot receiving data from the harvester application.
