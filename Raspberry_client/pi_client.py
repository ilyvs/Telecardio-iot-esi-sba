# import serial
from time import sleep
import paho.mqtt.client as mqtt
from datetime import datetime
import serial
import argparse
import json

# Parsing a line from the serial to extract the json payload
def parse_line(line):
    left = line.find("{")
    right = line.rfind("}")
    if(left != -1 and right != -1 ):
        return json.loads(line[left : right + 1])

    return False

if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description='Script to read vitals data from MySignal via serial and publish it through mqtt.')
    parser.add_argument('--broker', type=str, required=True,
                        help="The address of the MQTT broker.")

    args = parser.parse_args()
    
    broker = args.broker  

    try:
        client = mqtt.Client()
        client.connect(broker)
    except Exception as e:
        print(f"Failed to connect to MQTT broker {broker} with error:")
        print(e)

    # TODO: pass this argument through the cli
    #       search pyserial docs for a way to list the port automatically.
    ser = serial.Serial('/dev/ttyUSB0', baudrate=115200)
    while True:
        line = ser.readline()
        payload = parse_line(line)
        if(payload):
            payload["timestamp"] = datetime.now().timestamp()
            client.publish(payload['type'], json.dumps(payload))
            print(f'sent {payload}')
            sleep(1)
