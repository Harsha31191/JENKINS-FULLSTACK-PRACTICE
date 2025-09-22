pipeline {
  agent any

  // ensure the tool name here matches the name you added in
  // Manage Jenkins -> Global Tool Configuration (e.g. "Maven-3.9")
  tools {
    maven 'Maven-3.9'
  }

  stages {

    // ===== FRONTEND BUILD =====
    stage('Build Frontend') {
      steps {
        dir('frontend/DigitalWalletApp') {
          bat 'npm install'
          bat 'npm run build'
        }
      }
    }

    // ===== FRONTEND DEPLOY =====
    stage('Deploy Frontend to Tomcat') {
      steps {
        bat '''
        if exist "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\reactdigitalwalletapii" (
          rmdir /S /Q "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\reactdigitalwalletapii"
        )
        if not exist "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\reactdigitalwalletapi" (
          mkdir "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\reactdigitalwalletapi"
        )
        xcopy /E /I /Y frontend\\DigitalWalletApp\\dist\\* "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\reactdigitalwalletapi"
        '''
      }
    }

    // ===== BACKEND BUILD =====
    stage('Build Backend') {
      steps {
        dir('backend/digitalwallet_backend') {
          script {
            // Resolve the configured Maven installation (name must match Tools config)
            def mvnHome = tool name: 'Maven-3.9', type: 'hudson.tasks.Maven'
            // Use the absolute path to the mvn executable so we don't rely on PATH
            bat "\"${mvnHome}\\bin\\mvn\" clean package -DskipTests"
          }
        }
      }
    }

    // ===== BACKEND DEPLOY =====
    stage('Deploy Backend to Tomcat') {
      steps {
        bat '''
        if exist "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\springbootdigitalwalletapi.war" (
          del /Q "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\springbootdigitalwalletapi.war"
        )
        if exist "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\springbootdigitalwalletapi" (
          rmdir /S /Q "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\springbootdigitalwalletapi"
        )
        copy "backend\\digitalwallet_backend\\target\\*.war" "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\"
        '''
      }
    }

  } // stages

  post {
    success {
      echo 'Deployment Successful!'
    }
    failure {
      echo 'Pipeline Failed.'
    }
  }
}
