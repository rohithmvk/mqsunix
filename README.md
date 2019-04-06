
[MQ SENDER/ QUEUE DEPTH CHECKER – USER MANUAL]
 A command line interface to input bulk text messages into any remote MQ, by making use of configured MQ Queue manager and Sender Channel.

OVERVIEW

This tool will provide a command line interface to do below operations on any remote MQ, by making use of configured MQ Queue manager and Sender Channel.
i.    Bulk Upload of multiple messages into queues.
ii.    Checking depth of the queue (Get number of pending messages).
iii.    Retrieving messages present in the queue.
iv.    Clearing messages from the queue. 

Options:
a) ‘q’ to exit the tool console.
b) ‘mqc’ to view or edit the stored configs. 
c) Enter the ‘environment identifier’:  The “environment identifier” is a custom string defined in tool configuration, to identify the set of config we want to use. 

Entering the Environment ID as “INUAT1” will use the above configuration to read the file “data/INUAT1.TXT”, and input the content of this text file into queue XXX.XXXX.RS.XXX.XXIN, using  sender channel XXXXA.SVRCONN, and Queue manager AXXXXXMQ2, and port number 1414.
Configuring environment details :

Any number of configurations can be maintained in the config file. The format for the config set is:


PORT_NUM_<ENV_IDENTIFIER>=<Port number where the message is read (in-port) >
QUEUE_MANAGER_<ENV_IDENTIFIER>=<Queue Manager Name>
QUEUE_NAME_<ENV_IDENTIFIER>=<Queue Name>
CHANNEL_<ENV_IDENTIFIER>=<Sender Channel>
HOST_IP_ADDRESS_<ENV_IDENTIFIER>=<IP Address of Queue Manager Server>
REPLY_TO_QUEUE_<ENV_IDENTIFIER>=<Fill only if reply to Queue is to be set>
MESSAGE_FILE_<ENV_IDENTIFIER>=<Absolute path to Text file with the message to be sent>
NUM_MESSAGE_DISPLAY_<ENV_IDENTIFIER>=<Number of last messages to be displayed on Qdepth enquiry>
