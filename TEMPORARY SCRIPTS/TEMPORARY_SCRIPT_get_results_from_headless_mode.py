import csv

def process_text_file(input_file, output_file):
    with open(input_file, 'r') as f:
        lines = f.readlines()

    data = []
    seed = gold = bonus = score = None

    for line in lines:
        line = line.strip()
        if line.startswith("Seed"):
            seed = "'" + str(line.split(":")[1].strip())
        elif line.startswith("Gold collected"):
            gold = line.split(":")[1].strip()
        elif line.startswith("Bonus multiplier"):
            bonus = line.split(":")[1].strip()
        elif line.startswith("Score"):
            score = line.split(":")[1].strip()
            data.append([seed, gold, bonus, score])

    with open(output_file, 'w', newline='') as csvfile:
        csv_writer = csv.writer(csvfile)
        csv_writer.writerow(['Seed', 'Gold collected', 'Bonus multiplier', 'Score'])
        csv_writer.writerows(data)

if __name__ == "__main__":
    input_file = "console_output.txt"  # File containing the console output after a headless run
    output_file = "results.csv"  # Desired results file name
    process_text_file(input_file, output_file)
