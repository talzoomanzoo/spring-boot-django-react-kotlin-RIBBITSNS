from django.db import models
from django.utils.timezone import now

# Create your models here.
class image(models.Model):
    keyword = models.CharField(max_length=255)
    image_url=models.URLField(null=True)

    def __str__(self):
        return self.keyword

class Profile(models.Model):
    keyword = models.CharField(max_length=100)
    profile_dir=models.ImageField()
    profile_url=models.URLField(null=True)
    write_date = models.DateTimeField(default=now, editable=False)
    write_id = models.CharField(max_length=50)

    def __str__(self):
        return self.keyword