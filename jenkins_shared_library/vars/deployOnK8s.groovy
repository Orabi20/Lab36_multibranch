def call(String apiServer, String token, String namespace, String imageName, String deploymentFilePath) {
    def fileName = deploymentFilePath.tokenize("/").last()
    def dirPath = deploymentFilePath.replace("/${fileName}", "")

    dir(dirPath) {
        sh """
            sed -i 's|image:.*|image: ${imageName}|' ${fileName}
            cat ${fileName}
        """
        sh """
            kubectl --server=${apiServer} \
                    --token=${token} \
                    --insecure-skip-tls-verify \
                    apply -f ${fileName} -n ${namespace} --validate=false
        """
    }
}

