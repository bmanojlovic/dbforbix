java -Duser.language=en -Duser.country=US -Dlog4j.configuration=./conf/log4j.properties -cp $(for i in lib/*.jar ; do echo -n $i: ; done).:./projectjar mainclass start ./conf/config.props &
