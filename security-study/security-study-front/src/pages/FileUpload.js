import axios from "axios";

const FileUpload = () => {

    const fileList = []

    const onSaveFile = (e) => {

        const uploadFile = Array.prototype.slice.call(e.target.files);

        uploadFile.forEach((element) => {
            fileList.push(element);
        });
    }

    const onFileUpload = () => {
        const formData = new FormData();

        fileList.forEach((file) => {
            formData.append('multipartFiles', file);
        })

        axios.post('api/uploadFiles', formData)
        .then((response) => {
            console.log("response 왔어용")
            console.log(response.data)
        });

    }


    return (
        <div>
            <input type="file" multiple onChange={onSaveFile} />
            <button onClick={onFileUpload}>파일 업로드</button>
        </div>
    )
}

export default FileUpload;