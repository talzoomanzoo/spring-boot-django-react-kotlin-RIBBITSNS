from django.urls import path
from . import views
from django.conf import settings
from django.conf.urls.static import static


app_name = 'karlo'
urlpatterns = [

    path('', views.making_image, name='index'),
    
    path('profile/', views.save_selected_image, name='save_selected_image'),
    path('view/', views.view, name='view'),

    #views.py
]

if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)