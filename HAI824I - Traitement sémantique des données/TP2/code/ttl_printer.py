def pttl(k, v):
    print(k)
    for prop in v:
        if isinstance(v[prop], dict):
            print(prop,": {")
            for sub_prop in v[prop]:
                print(f"   {sub_prop} : {v[prop][sub_prop]}")
            print("}")
        else:
            print(f"{prop} : {v[prop]}")
    print("\n")