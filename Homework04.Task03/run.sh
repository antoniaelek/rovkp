 #!/bin/bash
/opt/spark-1.5.0-bin-hadoop2.6/bin/spark-submit --master local[2] target/SparkStreamingWordCount-1.0-SNAPSHOT.jar output
