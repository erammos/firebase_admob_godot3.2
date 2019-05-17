extends Node
var instance

# Called when the node enters the scene tree for the first time.
func _ready():
	if Engine.has_singleton("AdMob"):
		instance = Engine.get_singleton("AdMob")
	else:
		print("Admob singleton is only available on Android devices.")
	if instance:
		instance.getInstanceId(get_instance_id())

func load_rewarded_video():
	instance.request_rewarded_video("ca-app-pub-3940256099942544/5224354917")
	print("Loading...")
	
func show_rewarded_video():
	instance.show_rewarded_video()
	
func _on_rewarded_video_loaded():
	print("Video Loaded")
	
func _on_rewarded(var value):
	print("Reward: " + String(value))
	
func _on_rewarded_video_closed():
	print("Video Closed")
	
func _on_rewarded_video_failed():
	print("Video Failed")
	
func _on_rewarded_video_completed():
	print("Video Completed")
	
func _on_rewarded_video_opened():
	pass
func _on_rewarded_video_started():
	pass
func _on_rewarded_video_leftApplication():
	pass
	
