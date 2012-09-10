java -Duser.language=en \
	-Duser.country=US \
	-Dlog4j.configuration=./conf/log4j.properties \
	-cp $(for i in lib/*.jar ; do echo -n $i: ; done).:./dist/dbforbix.jar \
	com.smartmarmot.dbforbix.bootstrap start ./conf/config.props
