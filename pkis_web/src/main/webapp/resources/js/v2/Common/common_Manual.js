function getPhysicalFile(file) {
    var dlURL = `${getApiUrl()}${path_API_Version}FILE/DownloadPhysicalFile?file=${file}`;
    console.log(`${file}, ${dlURL}`);
    window.location.href = dlURL;
}