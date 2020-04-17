FROM pindar/jq as jq

FROM gradle:6.3.0-jdk8 

COPY --from=jq /usr/local/bin/jq /usr/local/bin/jq
RUN chmod +x /usr/local/bin/jq

USER root
WORKDIR /opt/test-runner
COPY bin/ /opt/test-runner/bin/
COPY lib/ /opt/test-runner/lib/
WORKDIR /opt/test-runner
RUN cd /opt/test-runner/lib/buildSrc; \
    gradle clean build; \
    cp -r ~/.gradle/ /opt/test-runner/.gradle/ 

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
