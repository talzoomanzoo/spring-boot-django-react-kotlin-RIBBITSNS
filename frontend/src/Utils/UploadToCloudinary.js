export const uploadToCloudinary = async (pics,fileType) => {
    if (pics) {
      
      const data = new FormData();
      console.log("picsform", pics);
      data.append("file", pics);
      data.append("upload_preset", "vo09fwkv");
      data.append("cloud_name", "dedvvc7cr");
  
      const res = await fetch(``, {
        method: "post",
        body: data,
      })
        
        const fileData=await res.json();
        console.log("url : ", fileData.url.toString());
        return fileData.url.toString();
  
    } else {
      console.log("error");
    }
  };