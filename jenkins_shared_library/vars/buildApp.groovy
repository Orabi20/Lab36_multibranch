def call() {
    dir('Jenkins/Lab35_sharedlib_agent') {
        sh 'mvn clean package -DskipTests'
    }
}

