#!/bin/bash

# desc: exec this command in root dir. gengerate template in target archetype and install

mvn clean archetype:create-from-project && cd target/generated-sources/archetype && mvn install

# mvn archetype:generate create maven project