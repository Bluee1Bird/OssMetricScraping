import io
import zipfile

import requests
import json
import os
import subprocess

# # Query parameters
# topic = "wordpress"
# language = "Java"  # Added language parameter
# created_from = "2016-01-01"
# created_to = "2016-12-31"
# pushed_from = "2016-01-01"
# per_page = 100
# page = 1
#
# # Build the query URL with the language parameter included
# #https://api.github.com/search/repositories?q=wordpress-plugin+language:Java&created:2016-01-01..2016-12-31&pushed:>=2024-04-01&per_page=100&page=1
# url = f"https://api.github.com/search/repositories?q=topic:{topic}+language:{language}&created:{created_from}..{created_to}&pushed:>={pushed_from}&per_page={per_page}&page={page}"
# print("url;",url)
#
# # Make the API request
# response = requests.get(url)
# print(response)
#
# # Check if the request was successful
# if response.status_code == 200:
#     data = response.json()
#
#     # store the results in a file
#     filename = f"{topic}_{created_from}_to_{created_to}_pushed_{pushed_from}.json"
#     # Save the data to a JSON file
#     with open(filename, 'w') as file:
#         json.dump(data, file)
#     print("Data saved to", filename)
#
#     # extract all the info to download the source code
#     project_extract_info = {}
#     for item in data["items"]:
#         project_extract_info[item["name"]] = [item["name"], item["url"], item["default_branch"]]
#     # print("extract info;", project_extract_info)

#manually selected projects
#projects = [["lbry-android","https://github.com/lbryio/lbry-android", "master"], ["GeometricWeather", "https://github.com/WangDaYeeeeee/GeometricWeather", "master"], ["talon-for-twitter-android", "https://github.com/klinker24/talon-for-twitter-android", "master"], ["lawnchair", "https://github.com/LawnchairLauncher/lawnchair", "14-dev"], ["litho", "https://github.com/facebook/litho", "master"], ["PictureSelector", "https://github.com/LuckSiege/PictureSelector", "version_component"], ["SmarterStreaming", "https://github.com/daniulive/SmarterStreaming", "master"] ]
#projects = [["lucida", "https://github.com/claritylab/lucida", "master"]]
#projects = [["phimpme-android", "https://github.com/fossasia/phimpme-android", "development"]]
#projects = [["h2o-2", "https://github.com/h2oai/h2o-2", "master"]]
#projects = [["anonymouth", "https://github.com/psal/anonymouth", "master"]]
#projects = [["word", "https://github.com/ysc/word", "master"]]
#projects = [["beam", "https://github.com/apache/beam", "master"]]
projects = [["PojavLauncher", "https://github.com/PojavLauncherTeam/PojavLauncher", "v3_openjdk"], ["hudi","https://github.com/apache/hudi", "master"], ["NekoX", "https://github.com/NekoX-Dev/NekoX", "main"]]

# download the source code for every repo
#for project in project_extract_info.values():
for project in projects:
    project_name = project[0]
    os.makedirs(project_name)

    url = project[1] + "/zipball/" + project[2]
    print(url)

    # The local file path to save the downloaded zip file
    # output_file = project[0] + '/repo.zip'

    # Make the request to download the zipball
    repo_response = requests.get(url, stream=True)

    # Check if the request was successful
    if repo_response.status_code == 200:
        # Wrap the response's raw file-like object with a BytesIO buffer
        # This allows us to use it as if it were a file on disk
        zip_file_bytes = io.BytesIO(repo_response.content)

        # Open the ZIP file
        with zipfile.ZipFile(zip_file_bytes, 'r') as zip_file:
            # Extract all the contents of the zip file into the output directory
            zip_file.extractall(project_name)
        print('Extraction complete!')

        # c&k metric calculation via cmd
        print("calculating c&k metrics")

        # figure out the exact path of the extracted file
        dirs = os.listdir(path=f'{project_name}')
        print("directories in there; ", dirs)
        print("list comprehension result ", [dir for dir in dirs if f"{project_name}" in dir])

        extracted_directory = [dir for dir in dirs if f"{project_name}" in dir][0]
        current_directory = os.getcwd()
        print("extracted dir: ", extracted_directory)
        project_dir = f'{current_directory}\\{project_name}\\{extracted_directory}\\'
        output_dir = f'{current_directory}\\{project_name}\\'

        # project dir has to be an absolute path (doesn't seem to be one)
        print("project directory in python script; ", project_dir)
        command = f"java -jar ck-0.7.1-SNAPSHOT-jar-with-dependencies.jar {project_dir} false 0 false {output_dir} 'build/' 'temp/' "
        directory = r"C:\Users\maaik\OneDrive\Dokumente\Uni\Semester 6\ESE Seminar\ck\target"

        # Change directory and execute the command
        result = subprocess.run(f"cd {directory} && {command}", shell=True, text=True, capture_output=True)

        # Print output and errors
        print("Output:", result.stdout)
        print("Error:", result.stderr)

    else:
        print(f'Failed to download file: {repo_response.status_code}')



# else:
#     print("Failed to fetch data from GitHub API:", response.status_code)

