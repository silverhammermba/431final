JAVA=$(wildcard *.java)

build/redrovers.jar: classes
	cd build; jar cvf $(notdir $@) *

classes: $(JAVA)
	mkdir -p build
	javac -d build -classpath ../javaagents/target/javaagents-2.1.jar $(JAVA)
	touch classes

run:
	rm -rf out
	../run.sh out conf ../javaagents/scripts/conf/dummy10b

clean:
	rm -rf build classes
