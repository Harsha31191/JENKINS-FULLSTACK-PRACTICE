pipeline {
  agent any

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

    // ===== BACKEND BUILD (absolute mvn) =====
    stage('Build Backend') {
      steps {
        dir('backend/digitalwallet_backend') {
          // absolute path to your mvn executable (adjust only if different)
          bat '"C:\\cicd\\apache-maven-3.9.11\\bin\\mvn" clean package -DskipTests'
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
    success { echo 'Deployment Successful!' }
    failure { echo 'Pipeline Failed.' }
  }
}
