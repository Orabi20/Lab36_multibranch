def call(String imageName) {
    sh """
        docker rmi ${imageName} || true
    """
}

