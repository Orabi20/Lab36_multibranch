def call(String apiServer, String token, String namespace, String imageName, String deploymentFilePath) {
    def fileName = deploymentFilePath.tokenize("/").last()
    def dirPath = deploymentFilePath.replace("/${fileName}", "")

    dir(dirPath) {
        sh """
            echo "Replacing image with: ${imageName}"
            sed -i 's|image:.*|image: ${imageName}|' ${fileName}
            echo "--- Deployment File After Image Replace ---"
            cat ${fileName}
        """

        sh """
            echo "Deploying to namespace: ${namespace}"
            kubectl --server=${apiServer} \\
                    --token=${token} \\
                    --insecure-skip-tls-verify \\
                    apply -f ${fileName} -n ${namespace} --validate=false
        """
    }
}
