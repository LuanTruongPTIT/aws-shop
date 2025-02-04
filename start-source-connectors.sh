curl -i -X PUT -H  "Content-Type:application/json" \
    http://localhost:8083/connectors/account-connector/config \
    -d @kafka/connects/debezium-account.json