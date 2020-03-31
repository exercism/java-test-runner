FROM pindar/jq

FROM gradle:6.3.0-jdk8 

COPY --from=0 /usr/local/bin/jq /usr/local/bin/jq
RUN chmod +x /usr/local/bin/jq

WORKDIR /opt/test-runner
COPY bin/ /opt/test-runner/bin/
COPY lib/ /opt/test-runner/lib/

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
