import barrel_gen
import crucible_gen
import sieve_gen
import ore_gen
import infested_gen


def main():
    print("Generating data...")
    barrel_gen.generate()
    crucible_gen.generate()
    sieve_gen.generate()
    ore_gen.generate()
    infested_gen.generate()
    print("Done!")


if __name__ == "__main__":
    main()
