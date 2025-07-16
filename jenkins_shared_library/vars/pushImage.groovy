def call(String imageName, String dockerUser, String dockerPass) {

    sh """
        echo "${dockerPass}" | docker login -u "${dockerUser}" --password-stdin
        docker push ${imageName}
        docker logout
    """
}

