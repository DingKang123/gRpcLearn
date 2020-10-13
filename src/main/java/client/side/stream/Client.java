package client.side.stream;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import stream.Stream.RequestData;
import stream.Stream.ResponseData;
import stream.StreamServiceGrpc;

public class Client {

	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8883).usePlaintext(true).build();
		StreamServiceGrpc.StreamServiceStub asyncStub = StreamServiceGrpc.newStub(channel);

		StreamObserver<ResponseData> responseData = new StreamObserver<ResponseData>() {
			@Override
			public void onNext(ResponseData value) {
				// TODO Auto-generated method stub
				System.out.println(value.getText());
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				t.printStackTrace();
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				channel.shutdown();
			}
		};

		StreamObserver<RequestData> requestData = asyncStub.clientSideStreamFun(responseData);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			requestData.onNext(RequestData.newBuilder().setText("hello" + i).build());
		}
		requestData.onCompleted();
		System.out.println(System.currentTimeMillis() - start + " MS");
		Thread.sleep(10000);
	}
}
