ip=$(/sbin/ip -o -4 addr list wlp3s0 | awk '{print $4}' | cut -d/ -f1)
port=4711
cd Station
gradle run --args="1 $ip $port" &

