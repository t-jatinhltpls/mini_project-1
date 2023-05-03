



# 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

import openai
import csv
# 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
openai.api_key = "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"

def is_function_buggy(code):
    # Set up the OpenAI GPT-3 API parameters
    prompt = "Is the following  code buggy? write answer in yes/no/undefined only  \n" + code + "\n Answer:"
    model = "text-davinci-002"
    temperature = 0.5
    max_tokens = 1
    stop = "\n"
    # Call the OpenAI GPT-3 API to get the response
    response = openai.Completion.create(engine=model, prompt=prompt, max_tokens=max_tokens, temperature=temperature, stop=stop)
    # Check if the response indicates that the code is buggy or not
    if response.choices[0].text.strip().lower() == "yes":
        return "yes"
    elif response.choices[0].text.strip().lower() == "undefined":
        return "undefined"
    else:
        return "no"



# Open the CSV file for reading and writing
with open('buggChecker\input.csv', 'r') as input_file, open('buggChecker\output.csv', 'w', newline='') as output_file:
    # Create CSV reader and writer objects
    reader = csv.reader(input_file)
    writer = csv.writer(output_file)
    print(reader)
    # Iterate over each row in the CSV file
    for row in reader:
        # Get the function code and determine if it is buggy
        function_code = row[2]
        is_buggy = is_function_buggy(function_code)

        # Update the row with the new boolean entry
        row.append(str(is_buggy))
        
        # Write the updated row to the output CSV file
        writer.writerow(row)
