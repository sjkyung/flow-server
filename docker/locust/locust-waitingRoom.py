from locust import FastHttpUser, task, between, stats

stats.PERCENTILES_TO_CHART = [0.5, 0.95, 0.99]

class WaitingRoomUser(FastHttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        # 유저마다 고유한 ID 부여
        # hash(self) 또는 unique_counter도 가능
        self.user_id = id(self)  # 또는 random.randint(1, 10_000)

    @task
    def enter_waiting_room(self):
        self.client.get("/waiting-room", params={
            "user_id": self.user_id,
            "redirect_url": "http://host.docker.internal:8500/"
        })