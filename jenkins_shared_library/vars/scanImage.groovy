def call(String imageName) {
    sh """
        echo "Simulating image scan on ${imageName}..."
        # Example for Trivy (if installed):
        # trivy image --exit-code 0 ${imageName}
    """
}

