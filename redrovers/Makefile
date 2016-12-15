JAVA=$(wildcard *.java)

build/redrovers.jar: classes
	cd build; jar cvf $(notdir $@) *

classes: $(JAVA)
	mkdir -p build
	javac -Xlint -encoding utf8 -d build -classpath ../javaagents/target/javaagents-2.1.jar $(JAVA)
	touch classes

doc: $(JAVA)
	rm -rf doc
	mkdir doc
	javadoc -d doc -classpath ../javaagents/target/javaagents-2.1.jar $(JAVA)

run: build/redrovers.jar
	rm -rf out
	../run.sh out conf ../javaagents/scripts/conf/dummy10b

replay:
	java -Xss20000k -cp ../massim/target/agentcontest-2013-1.4.jar massim.competition2013.monitor.GraphFileViewer -dir $(wildcard out/xmls/*)

run2: build/redrovers.jar
	rm -rf out2
	../run.sh out2 conf confb

replay2:
	java -Xss20000k -cp ../massim/target/agentcontest-2013-1.4.jar massim.competition2013.monitor.GraphFileViewer -dir $(wildcard out2/xmls/*)

clean:
	rm -rf build classes
