{
  description = "A flake for Spring Boot Development";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    let
      inherit (nixpkgs) lib;
      system = "x86_64-linux"; # Adjust this to match your target system

      pkgs = import nixpkgs {
        inherit system;
      };

      javaPkgs = with pkgs; [
        jdk17
        jdt-language-server
        maven
      ];
    in flake-utils.lib.eachSystem [ "x86_64-linux" ] (system:
      let
        pkgs = import nixpkgs { inherit system; };
      in {
        devShells.default = pkgs.mkShell {
          buildInputs = javaPkgs ++ [ pkgs.bash
                                      pkgs.pre-commit
                                      pkgs.drill
                                      pkgs.astyle
                                      pkgs.unzip
                                      pkgs.curl ];
          JAVA_HOME = lib.findFirst (p: p.name == "jdk17") null javaPkgs;

          shellHook = ''
            export JAVA_HOME=$JAVA_HOME
            echo "Setting up development environment for Spring Boot..."
            echo "Java Home: $JAVA_HOME"
            echo "Maven Version: $(mvn --version)"
            if [ ! -f .git/hooks/pre-commit ]; then
              pre-commit install
            fi
            if [ ! -f pom.xml ]; then
              echo pom.xml not found, generating base project...
              echo TODO list all preferences
              echo TODO check errors
              curl --silent https://start.spring.io/starter.zip \
                   -d dependencies=web,devtools,lombok,h2,jpa \
                   -d name=todolist -d groupId=br.edu.ifba \
                   -d description="Demo ToDo List" -d packageName=br.edu.ifba.todolist \
                   -d bootVersion=3.3.3 -d packaging=jar -d artifactId=todolist \
                   -d javaVersion=17 -d type=maven-project -o base.zip
              unzip -nq base.zip
              rm base.zip
            fi
          '';
        };
      }
    );
}
