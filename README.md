# BOTICA - RESTest

> [!NOTE]
> This project was created using the [BOTICA template for Java](https://github.com/migromarj/BOTICA-java-template).

## Running RESTest with BOTICA

1. **Clone or download the project from the repository.**

2. **Install BOTICA as a local dependency, following the steps described at [BOTICA README](https://github.com/migromarj/BOTICA#installing-botica-as-a-local-dependency).**

3. **Compiling the project.**

    Open a terminal at the project's root and run the following Maven command to compile and package the project.
    ```
    mvn clean install
    ```

4. **Generate the necessary files to launch the BOTICA environment.**

    - To use the default file located in `src/main/resources/BOTICAConfig/configuration-setup.properties`, run the following maven command at the root of the project:
        ```
        mvn exec:java@configuration-setup
        ```
    - To use a file located in another path, run the following maven command at the root of the project:
        ```
        mvn exec:java@configuration-setup -Dexec.args="path/to/file"
        ```

5. **Launch the BOTICA environment.**

    In case you are using **Windows operating system**, you should run the following maven command at the root of the project:

    ```
    mvn exec:exec@launch-botica-windows
    ```
    
    If, on the other hand, you are using a **Unix-based system**, you should run the following maven command at the root of the project:
    
    ```
    mvn exec:exec@launch-botica-unix
    ```

6. **Launch the data collector bot.**

   - To use the default file located in `src/main/resources/BOTICAConfig/collector.properties`, run the following maven command at the root of the project:
       ```
       mvn exec:java@launch-collector
       ```
   - To use a file located in another path, run the following maven command at the root of the project:
       ```
       mvn exec:java@launch-collector -Dexec.args="path/to/file"
       ```
