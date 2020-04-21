FROM pindar/jq as jq

FROM gradle:6.3.0-jdk8 

COPY --from=jq /usr/local/bin/jq /usr/local/bin/jq
RUN chmod +x /usr/local/bin/jq


COPY lib/build.gradle /opt/test-runner/lib/build.gradle
COPY lib/settings.gradle /opt/test-runner/lib/settings.gradle

COPY lib/buildSrc/build.gradle /opt/test-runner/lib/buildSrc/build.gradle
COPY lib/buildSrc/src /opt/test-runner/lib/buildSrc/src

WORKDIR /opt/test-runner
RUN cd /opt/test-runner/lib/buildSrc; \
    gradle clean build; \
    mkdir /opt/test-runner/gradle; \
    cp -r ~/.gradle/ /opt/test-runner/gradle/.gradle/; \
    unlink /root/.gradle; \
    ln -s /opt/test-runner/gradle/.gradle /root/.gradle

COPY bin/ /opt/test-runner/bin/
ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
