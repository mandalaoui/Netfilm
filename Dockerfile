FROM gcc:latest

RUN apt-get update && apt-get install -y cmake g++ make 

COPY . /usr/src/mytest

WORKDIR /usr/src/mytest 

RUN mkdir build
WORKDIR /usr/src/mytest/build

VOLUME ["/usr/src/mytest/data"]

RUN cmake --version

RUN cmake .. && make

# Run tests
CMD ["./runTests"]