ip=$(/sbin/ip -o -4 addr list wlp3s0 | awk '{print $4}' | cut -d/ -f1)
port=4711
interval=5000
cd Sensor

gradle run --args="sensor1 wind $ip $port $interval" &
gradle run --args="sensor2 rain $ip $port $interval" &
gradle run --args="sensor3 temperature $ip $port $interval" &
gradle run --args="sensor4 humidity $ip $port $interval" &

