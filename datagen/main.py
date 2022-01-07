import barrel_gen
import crucible_gen
import sieve_gen


def main():
    barrel_gen.generate()
    crucible_gen.generate()
    sieve_gen.generate()


if __name__ == "__main__":
    print("Generating data...")
    main()
    print("Done.")
