# MLSecOps - DevSecOps - Awesome

This project is dedicated to curating a comprehensive list of resources, tools, and best practices at the intersection of Machine Learning Security Operations (MLSecOps), and Development Security Operations (DevSecOps). Our goal is to provide a centralized hub for professionals, researchers, and enthusiasts who are passionate about integrating security into the development and deployment of machine learning systems.

## What is MLSecOps?

MLSecOps is an emerging field that focuses on the secure and efficient operation of machine learning models in production environments. It combines the principles of DevSecOps with the unique challenges of machine learning, emphasizing the importance of security, privacy, and compliance throughout the ML lifecycle.

## What is DevSecOps?

DevSecOps extends the traditional DevOps framework by incorporating security practices into the entire software development process. It aims to automate security checks and integrate them seamlessly into the CI/CD pipeline, ensuring that security is a fundamental part of the development workflow.

## Repository Overview

In this repository, you will find:

* Resources: Articles, papers, and tutorials on MLSecOps and DevSecOps.
* Tools: A curated list of open-source tools for securing ML models and development pipelines.
* Best Practices: Guidelines and methodologies for implementing security measures in ML projects.
* Case Studies: Real-world examples of successful MLSecOps and DevSecOps implementations.
* Community: Links to forums, conferences, and groups where you can connect with others interested in these fields.

# Proposed Pipeline

### 💥 MLSecOps Pipeline

![image](https://github.com/user-attachments/assets/da731753-0ea2-4292-aa37-013d46e51e76)

### 💥 DevSecOps Pipeline

![image](https://github.com/user-attachments/assets/223d4897-ccc2-434c-b31a-ca74a9fbc42f)

## Research papers List

## Cousers

## Tools

<table>
  <thead>
    <tr>
      <th>Pipeline</th>
      <th>Stages</th>
      <th>Tool</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td rowspan="30">MLSecOps</td>
      <td rowspan="2">Stage 1</td>
      <td><a href="https://pre-commit.com/">Pre-Commit Hook Scans</td>
      <td>A framework for managing and maintaining multi-language pre-commit hooks.</td>
    </tr>
    <tr>
        <td>IDE plugins<br><a href="https://marketplace.visualstudio.com/items?itemName=AquaSecurityOfficial.trivy-vulnerability-scanner">Trivy Vulnerability Scanner</a><br><a href="https://marketplace.visualstudio.com/items?itemName=trunk.io">Trunk Check</a>
      </td>
        <td><br>Comprehensive vulnerability scanner for containers and other artifacts.<br>Automated Code Quality for Teams: universal formatting, linting, static analysis, and security.</td>
    </tr>
    <tr>
        <td rowspan="2">Stage 2</td>
        <td><a href="https://aws.amazon.com/s3/">AWS S3 bucket</a></td>
        <td>A bucket is a container for objects stored in Amazon S3.</td>
    </tr>
    <tr>
        <td><a href="https://www.sonatype.com/products/sonatype-nexus-repository">Nexus Repository</a></td>
        <td>Sonatype Nexus Repository</td>
    </tr>
    <tr>
        <td rowspan="6">Stage 3</td>
        <td><a href="https://gitleaks.io/">Gitleak</a></td>
        <td>Secret scanner for git repositories, files, and directories.</td>
    </tr>
    <tr>
        <td><a href="https://www.sonatype.com/products/sonatype-nexus-repository">Sonarqube</a></td>
        <td>Open-source platform for continuous inspection of code quality.</td>
    </tr>
    <tr>
        <td><a href="https://aquasecurity.github.io/trivy/">Trivy</a></td>
        <td>Comprehensive vulnerability scanner for containers and other artifacts.</td>
    </tr>
    <tr>
        <td><a href="https://horusec.io/">Horusec</a></td>
        <td>Tool to perform static code analysis to identify security flaws.</td>
    </tr>
    <tr>
        <td><a href="https://owasp.org/www-project-dependency-check/">OWASP Dependency-Check</a></td>
        <td>Tool that identifies project dependencies and checks for known vulnerabilities.</td>
    </tr>
    <tr>
        <td><a href="https://nbdefense.ai/">NB Defense</a></td>
        <td>Security tool for Jupyter notebooks, scanning for vulnerabilities and risks.</td>
    </tr>
    <tr>
    <tr>
        <td rowspan="2">Stage 4</td>
        <td><a href="https://keras.io/api/callbacks/early_stopping/">EarlyStopping</a></td>
        <td>Stop training when a monitored metric has stopped improving.</td>
    </tr>
    <tr>
        <td><a href="https://scikit-learn.org/stable/modules/generated/sklearn.model_selection.KFold.html">KFold</a></td>
        <td>K-Fold cross-validator.</td>
    </tr>
    <tr>
        <td rowspan="1">Stage 5</td>
        <td><a href="https://scikit-learn.org/stable/modules/model_evaluation.html">EarlyStopping</a></td>
        <td>Metrics and scoring: quantifying the quality of predictions.</td>
    </tr>
    <tr>
        <td rowspan="3">Stage 6</td>
        <td><a href="https://github.com/protectai/modelscan">modelscan</a></td>
        <td>Protection Against ML Model Serialization Attacks.</td>
    </tr>
    <tr>
        <td><a href="https://github.com/deadbits/vigil-llm">Vigil</a></td>
        <td>LLM prompt injection and security scanner.</td>
    </tr>
    <tr>
        <td><a href="https://github.com/leondz/garak">Garak</a></td>
        <td>LLM vulnerability scanner.</td>
    </tr>
    <tr>
        <td rowspan="1">Stage 7</td>
        <td><a href="https://github.com/noobpk/gemini-self-protector">gemini-self-protector</a></td>
        <td>Gemini - The Runtime Application Self Protection (RASP) Solution Combined With Deep Learning.</td>
    </tr>
  </tbody>
</table>


## Contribution

We welcome contributions from the community to help us expand and improve this repository. If you have suggestions, tools, or resources that you believe should be included, please feel free to submit a pull request or open an issue.

Thank you for visiting our repository. We hope you find it a valuable resource in your journey towards secure and effective machine learning operations.
