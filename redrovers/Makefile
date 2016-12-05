JAVA=$(wildcard *.java)

build/redrovers.jar: classes
	cd build; jar cvf $(notdir $@) *

classes: $(JAVA)
	mkdir -p build
	javac -Xlint -d build -classpath ../javaagents/target/javaagents-2.1.jar $(JAVA)
	touch classes

run: build/redrovers.jar
	rm -rf out
	../run.sh out conf ../javaagents/scripts/conf/dummy10b

replay:
	java -Xss20000k -cp ../massim/target/agentcontest-2013-1.4.jar massim.competition2013.monitor.GraphFileViewer -dir $(wildcard out/xmls/*)

clean:
	rm -rf build classes
