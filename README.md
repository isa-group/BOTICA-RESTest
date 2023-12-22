# BOTICA - RESTest

## Running RESTest with BOTICA

1. **Clone or download the project from the repository.**

2. **Install BOTICA as a local dependency, following the steps described at [BOTICA README](https://github.com/migromarj/BOTICA#installing-botica-as-a-local-dependency).**

3. **Compiling the project.**

    Open a terminal at the project's root and run the following Maven command to compile and package the project.
    ```
    mvn clean install
    ```

4. **Generate the necessary files to launch the BOTICA environment.**

    Run the following maven command at the root of the project:
    ```
    mvn exec:java@configuration-setup
    ```

5. **Launch the BOTICA environment.**

    Run the following maven command at the root of the project:
    ```
    mvn exec:exec@launch-botica
    ```

6. **Launch the data collector bot.**

   Run the following maven command at the root of the project:
   ```
   mvn exec:java@launch-collector
   ```
