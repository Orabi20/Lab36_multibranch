def call(String imageName) {
    dir('Jenkins/Lab35_sharedlib_agent') {
        sh """
            docker build -t ${imageName} .
        """
    }
}

